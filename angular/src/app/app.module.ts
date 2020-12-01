import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TodosComponent } from './todos/todos.component';
import { TopComponent } from './top/top.component';
import { TodoInsertComponent } from './todo-insert/todo-insert.component';
import { TodoUpdateComponent } from './todo-update/todo-update.component';
import { CategoriesComponent } from './categories/categories.component';
import { MessagesComponent } from './messages/messages.component';
import { CategoryInsertComponent } from './category-insert/category-insert.component';
import { CategoryUpdateComponent } from './category-update/category-update.component';

@NgModule({
  declarations: [
    AppComponent,
    TodosComponent,
    TopComponent,
    TodoInsertComponent,
    TodoUpdateComponent,
    CategoriesComponent,
    MessagesComponent,
    CategoryInsertComponent,
    CategoryUpdateComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
