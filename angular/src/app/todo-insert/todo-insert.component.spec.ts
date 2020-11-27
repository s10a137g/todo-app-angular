import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TodoInsertComponent } from './todo-insert.component';

describe('TodoInsertComponent', () => {
  let component: TodoInsertComponent;
  let fixture: ComponentFixture<TodoInsertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TodoInsertComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TodoInsertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
