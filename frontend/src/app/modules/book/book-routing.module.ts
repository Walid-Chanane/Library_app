import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './pages/main/main.component';
import { BookListComponent } from './pages/book-list/book-list.component';
import { MyBooksComponent } from './pages/my-books/my-books.component';
import { ManageBookComponent } from './pages/manage-book/manage-book.component';
import { BorrowedBookListComponent } from './pages/borrowed-book-list/borrowed-book-list.component';
import { ReturnedBooksComponent } from './pages/returned-books/returned-books.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent, // when we navigate to books we will load the main component
    children: [
      {
        path: '',
        component: BookListComponent
      },
      {
        path: 'my_books',
        component: MyBooksComponent
      },
      {
        path: 'manage',
        component: ManageBookComponent
      },
      {
        path: 'manage/:bookId',
        component: ManageBookComponent
      },
      {
        path: 'my_borrowed_books',
        component: BorrowedBookListComponent
      },
      {
        path: 'my_returned_books',
        component: ReturnedBooksComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }
