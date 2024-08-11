import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from '../../../../services/models';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
}) 
export class BookCardComponent {

  private _book: BookResponse = {}
  private _bookCover: string | undefined
  private _manage = false

  get book(): BookResponse{
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value
  }

  get bookCover(): string | undefined {
    if(this._book.cover){
      return 'data:image/jpg;base64,' + this._book.cover
    }
    return 'https://icon-library.com/images/books-icon-png/books-icon-png-29.jpg';
    // return 'https://www.freeiconspng.com/uploads/book-icon--icon-search-engine-6.png'
  }

  get manage(){
    return this._manage
  }

  @Input()
  set manage(value){
    this._manage = value
  }

  @Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  onShowDetails(){
    this.details.emit(this._book)
  }

  onBorrow(){
    this.borrow.emit(this._book)
  }

  onAddToWaitingList(){
    this.addToWaitingList.emit(this._book)
  }

  onEdit(){
    this.edit.emit(this._book)
  }

  onShare(){
    this.share.emit(this._book)
  }

  onArchive(){
    this.archive.emit(this._book)
  }
}
