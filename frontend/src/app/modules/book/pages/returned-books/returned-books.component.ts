import { Component, OnInit } from '@angular/core';
import { PageResponseBorrowedBookResponse, BorrowedBookResponse } from '../../../../services/models';
import { BookService } from '../../../../services/services';

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrl: './returned-books.component.scss'
})
export class ReturnedBooksComponent implements OnInit{

  returnedBooks: PageResponseBorrowedBookResponse = {}
  page: number = 0
  size: number = 5
  success: boolean = true
  message: string = ''
  
  constructor(
    private bookService: BookService
  ){}

  ngOnInit(): void {
    this.findAllReturnedBooks()
  }

  findAllReturnedBooks(){
    this.bookService.findReturnedBooks({
      page: this.page,
      size: this.size
    })
  }
  
  approveBookReturn(book: BorrowedBookResponse){
    if(!book.returned){
      this.success = false
      this.message = 'Book has not been returned'
      return
    }
    this.bookService.approveReturnedBook({
      'bookId': book.id as number
    }).subscribe({
      next: () => {
        this.success = true
        this.message = 'Book return approved'
        this.findAllReturnedBooks
      }
    })
  }

  goToFirstPage(){
    this.page = 0
    this.findAllReturnedBooks()
  }

  goToPreviousPage(){
    this.page--
    this.findAllReturnedBooks()
  }

  goToPage(index: number){
    this.page = index
    this.findAllReturnedBooks()
  }

  goToNextPage(){
    this.page++
    this.findAllReturnedBooks()
  }

  goToLastPage(){
    this.page = this.returnedBooks.totalPages as number -1
    this.findAllReturnedBooks()
  }

  get isLastPage(): boolean{
    return this.page == this.returnedBooks.totalPages as number -1
  }

}
