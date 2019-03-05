import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InteractiveRoutesComponent } from './interactive-routes.component';

describe('InteractiveRoutesComponent', () => {
  let component: InteractiveRoutesComponent;
  let fixture: ComponentFixture<InteractiveRoutesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InteractiveRoutesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InteractiveRoutesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
