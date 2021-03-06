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
      surname: 'surname',
      id: 1,
      login: 'login'
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display name and surname of user', () => {
    const displayedName = fixture.debugElement.nativeElement.querySelector('.contact-name').textContent;
    expect(displayedName).toContain(component.user.name);
    expect(displayedName).toContain(component.user.surname);
  });
});
