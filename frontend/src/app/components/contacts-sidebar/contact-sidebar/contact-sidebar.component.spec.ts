import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactSidebarComponent } from './contact-sidebar.component';

describe('ContactSidebarComponent', () => {
  let component: ContactSidebarComponent;
  let fixture: ComponentFixture<ContactSidebarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContactSidebarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactSidebarComponent);
    component = fixture.componentInstance;
    component.user = {
      name: 'name',
      surname: 'surname'
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
