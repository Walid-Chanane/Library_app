import { Component } from '@angular/core';
import { BookRequest } from '../../../../services/models';
import { BookService } from '../../../../services/services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent {

  bookRequest: BookRequest = {author: '', isbn: '', publisher: '', synopsis: '', title: ''}
  errors: Array<string> = []
  selectedBookCover: any
  selectedPicture: string | undefined

  constructor(
    private bookService: BookService,
    private router: Router
  ){  }

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
