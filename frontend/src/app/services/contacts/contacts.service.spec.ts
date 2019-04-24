import { TestBed, inject } from '@angular/core/testing';

import { ContactsService } from './contacts.service';

describe('ContactsService', () => {
  let service: ContactsService;

  beforeEach(() => TestBed.configureTestingModule({}));

  beforeEach(inject([ ContactsService ], (contactsService) => {
    service = contactsService;
  }));

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should to save contacts', () => {
    const contacts = [{
      id: 0,
      login: 'login0',
      name: 'name0',
      surname: 'surname0'
    }, {
      id: 1,
      login: 'login1',
      name: 'name1',
      surname: 'surname1'
    }];
    service.setContacts(contacts);
    expect(service.getContact(0)).toBe(contacts[0]);
    expect(service.getContact(1)).toBe(contacts[1]);
  });

  it('should not to return contact if it is not saved', () => {
    expect(service.getContact(0)).toBeUndefined();
  });
});
