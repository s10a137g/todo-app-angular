import { Injectable } from '@angular/core';
import { Category } from './category'
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service'
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class CategoryService {

  constructor(private http: HttpClient,
              private messageService: MessageService) { }

  private categoryDeleteUrl = 'http://localhost:9000/api/category/delete';
  private categoriesGetUrl = 'http://localhost:9000/api/categories/get';

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.categoriesGetUrl)  
  } 

  deleteCategory(id: number): Observable<Category> {
    const url = `${this.categoryDeleteUrl}/${id}`;
    return this.http.delete<Category>(url)  
  }
}
