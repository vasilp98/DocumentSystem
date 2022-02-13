import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DocumentLinksComponent } from './document-links.component';

describe('DocumentLinksComponent', () => {
  let component: DocumentLinksComponent;
  let fixture: ComponentFixture<DocumentLinksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumentLinksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentLinksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
