package com.meta.junitproject.service;

import com.meta.junitproject.domain.Book;
import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.util.MailSender;
import com.meta.junitproject.web.dto.response.BookRespDto;
import com.meta.junitproject.web.dto.request.BookSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Client -> (Filter) -> Dispatcher Servlet -> Controller -> Service -> Repository -> Persistence Context -> DB
 * Service에서 트랜잭션 시작, 종료. 따라서 종료되면 DB 변형 불가능
 * Controller에서 DB 세션 닫힘, 그래서 컨트롤러까지는 select가 가능하다.
 * 만약 Service가 Entity를 반환하게 되면, 문제가 생긴다.
 * Controller가 응답 시 메시지 컨버터가 작동하는데 이 컨버터가 해당 엔티티를 getter를 호출해 json으로 변경해 응답한다.
 * 이 과정에서 lazy loading 발생(by getter)
 * dto는 persistence 안에 없으므로 lazy loading이 일어나지 않는다. 그러니 service단에서 dto로 변환해 반환해야 한다.
 */
@RequiredArgsConstructor
@Service
public class BookService {

    // final: 객체 생성 시점에 값이 들어와야 함 -> RequiredArgsConstructor
    private final BookRepository bookRepository;
    private final MailSender mailSender;

    // 1. 책 등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto register_book(BookSaveReqDto dto) {
        // 영속화된 객체를 컨트롤러로 응답해주면 컨트롤러 단에서 lazy-loading 변수 발생
        // jpa: open in view: true시 컨트롤러 단까지 세션을 유지하는 형식이라 지연 로딩 가능
        Book bookPS = bookRepository.save(dto.toEntity());
        if (bookPS != null) {
            if (!mailSender.send()) {
                throw new RuntimeException("메일이 전송되지 않았습니다.");
            }
        }
        return bookPS.toDto();
    }

    // 2. 책 목록 보기
    // stream / filter / map(박스에 옮겨담기, 얘만의 스트림으로 바뀜) / collect(변환해서 수집/목적지로 전달)
    public List<BookRespDto> look_book_contents() {
        return bookRepository.findAll().stream()
//                .map((bookPS) -> bookPS.toDto())
                .map(Book::toDto)
                .collect(Collectors.toList());
    }

    // 3. 책 한 권 보기
    public BookRespDto look_one_book(Long id) {
        Optional<Book> bookOP = bookRepository.findById(id);
        if (bookOP.isPresent()) {
            Book bookPS = bookOP.get();
            return bookPS.toDto();
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }

    // 4. 책 삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete_book(Long id) { // 없는 id면 롤백할 필요가 없다. (DB에서 삭제를 안 했기 때문)
        bookRepository.deleteById(id);
    }

    // 5. 책 수정
    // Transactional 걸린 애들은 컨트롤러로 넘겨줄 필요 없음.
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto modify_book(Long id, BookSaveReqDto dto) { // id, title, author
        Optional<Book> bookOP = bookRepository.findById(id);
        if (bookOP.isPresent()) {
            Book bookPS = bookOP.get();
            bookPS.update(dto.getTitle(), dto.getAuthor());
            return bookPS.toDto();
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    } // method 종료시 더티 체킹(flush)으로 update된다.

}