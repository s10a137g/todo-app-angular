import { Component, OnInit } from '@angular/core';
import { Category } from '../category';
import { CategoryService } from '../category.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss']
})
export class CategoriesComponent implements OnInit {
  categories: Category[]
  
  constructor(private categoryService: CategoryService) { }
  
//   onSelect(category: Category): void {
//     this.selectedCategory = category;
//     this.messageService.add('CategoriesComponent: Selected category id=${todo.id}')
//   }

  getCategories(): void {
     this.categoryService.getCategories().subscribe(categories => this.categories = categories);
  }

  ngOnInit(): void {
    this.getCategories();
  }

}
