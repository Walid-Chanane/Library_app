import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './pages/main/main.component';
import { BookListComponent } from './pages/book-list/book-list.component';
import { MyBooksComponent } from './pages/my-books/my-books.component';

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
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }
