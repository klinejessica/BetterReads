package io.jessica.betterreads.book;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BookController {

    private final String COVER_IMG_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired 
    BookRepository bookRepository;

    @GetMapping(value="/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            String coverImgURL = "/images/no-image.png";
            if(book.getCoverIds() != null & book.getCoverIds().size()> 0){
                coverImgURL = COVER_IMG_ROOT + book.getCoverIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImg", coverImgURL);
            model.addAttribute("book", book);
            return "book";
        }
       return "book-not-found";
    }
    
    
}
