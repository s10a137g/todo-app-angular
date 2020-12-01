import { Component, OnInit } from '@angular/core';

import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

import { CategoryService } from '../category.service'
import { Category } from '../category'

@Component({
  selector: 'app-category-insert',
  templateUrl: './category-insert.component.html',
  styleUrls: ['./category-insert.component.scss']
})
export class CategoryInsertComponent implements OnInit {
  insertForm;

  constructor(
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private router: Router,
  ) { 
    this.insertForm = this.formBuilder.group({
      name: '',
      slug: '',
      color: '',
    })
  }

  ngOnInit(): void {
  }

  onCategoryInsertSubmit(categorydata: Category): void {
    this.categoryService.insertCategory(categorydata).subscribe(() => this.router.navigate(['/categories/list']))
  }

}
