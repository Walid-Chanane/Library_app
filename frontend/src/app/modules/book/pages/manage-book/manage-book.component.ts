import { Component, OnInit } from '@angular/core';
import { BookRequest, BookResponse } from '../../../../services/models';
import { BookService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit{

  bookRequest: BookRequest = {author: '', isbn: '', publisher: '', synopsis: '', title: ''}
  errors: Array<string> = []
  selectedBookCover: any
  selectedPicture: string | undefined

  constructor(
    private bookService: BookService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ){  }

  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.params['bookId']
    if(bookId){
      this.bookService.findBookById({
        'bookId': bookId
      }).subscribe({
        next: (book: BookResponse) => {
          this.bookRequest = {
            id: book.id,
            title: book.title as string,
            author: book.author as string,
            publisher: book.publisher as string,
            isbn: book.isbn as string,
            synopsis: book.synopsis as string,
            shareable: book.shareable
          }
          if(book.cover){
            this.selectedPicture = 'data:image/jpg;base64,' + book.cover
          }
        }
      })
    }
  }

  onFileSelect(event: any){
    this.selectedBookCover = event.target.files[0]
    console.log(this.selectedBookCover)
    if(this.selectedBookCover){
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string
      }
      reader.readAsDataURL(this.selectedBookCover)
    }
  }

  saveBook(){
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        this.bookService.uploadBookCoverPicture({
          'bookId': bookId,
          body: {
            file: this.selectedBookCover
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/books/my_books'])
          }
        })
      },
      error: (err) => {
        this.errors = err.error.validationErrors
      }
    })
  }
}
