import { Component, OnInit } from '@angular/core';
import { BorrowedBookResponse, FeedbackRequest, PageResponseBorrowedBookResponse } from '../../../../services/models';
import { BookService, FeedbackService } from '../../../../services/services';

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit{
  
  borrowedBooks: PageResponseBorrowedBookResponse = {}
  page: number = 0
  size: number = 5
  selectedBook: BorrowedBookResponse | undefined = undefined
  feedbackRequest: FeedbackRequest = {
    bookId: 0,
    comment: '',
    evaluation: 0
  }
  
  constructor(
    private bookService: BookService,
    private feedbackService: FeedbackService
  ){}

  ngOnInit(): void {
    this.findAllBorrowedBooks()
  }

  findAllBorrowedBooks(){
    this.bookService.findBorrowedBooks({
      page: this.page,
      size: this.size
    })
  }
  
  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book
    this.feedbackRequest.bookId = book.id as number
  }
  
  goToFirstPage(){
    this.page = 0
    this.findAllBorrowedBooks()
  }

  goToPreviousPage(){
    this.page--
    this.findAllBorrowedBooks()
  }

  goToPage(index: number){
    this.page = index
    this.findAllBorrowedBooks()
  }

  goToNextPage(){
    this.page++
    this.findAllBorrowedBooks()
  }

  goToLastPage(){
    this.page = this.borrowedBooks.totalPages as number -1
    this.findAllBorrowedBooks()
  }

  get isLastPage(): boolean{
    return this.page == this.borrowedBooks.totalPages as number -1
  }

  returnBook(withFeedback: boolean){
    this.bookService.returnBorrowedBook({
      'bookId': this.selectedBook?.id as number
    }).subscribe({
      next: () => {
        if(withFeedback){
          this.sendFeedback()
        }
        this.selectedBook = undefined
        this.findAllBorrowedBooks()
      }
    })
  }

  private sendFeedback(){
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: () => {}
    })
  }
}
