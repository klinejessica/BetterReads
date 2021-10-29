package io.jessica.betterreads.book;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.method.annotation.PrincipalMethodArgumentResolver;

import io.jessica.betterreads.userbooks.UserBooks;
import io.jessica.betterreads.userbooks.UserBooksPrimaryKey;
import io.jessica.betterreads.userbooks.UserBooksRepository;


@Controller
public class BookController {

    private final String COVER_IMG_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired 
    BookRepository bookRepository;

    @Autowired
    UserBooksRepository userBooksRepository;

    @GetMapping(value="/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principle) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            String coverImgURL = "/images/no-image.png";
            if(book.getCoverIds() != null & book.getCoverIds().size()> 0){
                coverImgURL = COVER_IMG_ROOT + book.getCoverIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImg", coverImgURL);
            model.addAttribute("book", book);

            if(principle != null && principle.getAttribute("login") != null){
                String userId = principle.getAttribute("login");
                model.addAttribute("loginId", principle.getAttribute("login"));
                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setBookId(bookId);
                key.setUserId(userId);
                Optional<UserBooks> userBooks = userBooksRepository.findById(key);
                if(userBooks.isPresent()){
                    model.addAttribute("userBooks", userBooks.get());
                } else {
                    model.addAttribute("userBooks", new UserBooks());
                }
            }
 
            return "book";
        }
       return "book-not-found";
    }
    
    
}
