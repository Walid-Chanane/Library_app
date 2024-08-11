import { Component, OnInit } from '@angular/core';
import { BookResponse, PageResponseBookResponse } from '../../../../services/models';
import { Router } from '@angular/router';
import { BookService } from '../../../../services/services';

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit{
  bookResponse: PageResponseBookResponse = {}
  page = 0
  size = 5

  constructor(private bookService: BookService, private router: Router){
  }

  ngOnInit(): void {
    this.findMyBooks()
  }

  private findMyBooks(){ // size and page are optionnal
    this.bookService.findBooksByOwner({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (books) => {
        this.bookResponse = books
      }
    })
  }

  goToFirstPage(){
    this.page = 0
    this.findMyBooks()
  }

  goToPreviousPage(){
    this.page--
    this.findMyBooks()
  }

  goToPage(index: number){
    this.page = index
    this.findMyBooks()
  }

  goToNextPage(){
    this.page++
    this.findMyBooks()
  }

  goToLastPage(){
    this.page = this.bookResponse.totalPages as number -1
    this.findMyBooks()
  }

  get isLastPage(): boolean{
    return this.page == this.bookResponse.totalPages as number -1
  }

  
  archiveBook(book: BookResponse){
    this.bookService.updateArchivedStatus({
      'bookId': book.id as number
    }).subscribe({
      next: () => {
        book.archived = !book.archived
      }
    })
  }

  shareBook(book: BookResponse){
    this.bookService.updateShareableStatus({
      'bookId': book.id as number
    }).subscribe({
      next: () => {
        book.shareable = !book.shareable
      }
    })
  }

  editBook(book: BookResponse){
    this.router.navigate(['books', 'manage', book.id]) // basically '/books/manage/{bookId}'
  }

}
