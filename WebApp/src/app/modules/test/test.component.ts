import { Component, ViewChild, OnInit, Output, EventEmitter, ElementRef, AfterViewInit } from '@angular/core';
import { Subject } from 'rxjs';
import WebViewer, { WebViewerInstance } from '@pdftron/webviewer';
import {DataService} from "@core/services/data.service";

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss']
})
export class TestComponent implements OnInit, AfterViewInit {
  @ViewChild('viewer') viewer: ElementRef;
  wvInstance: WebViewerInstance;
  @Output() coreControlsEvent:EventEmitter<string> = new EventEmitter();
  private documentLoaded$: Subject<void>;

  constructor(private dataService: DataService) {
    this.documentLoaded$ = new Subject<void>();
  }



  ngAfterViewInit(): void {
    WebViewer({
      path: '../../../lib'
    }, this.viewer.nativeElement).then(instance => {
      this.wvInstance = instance;

      this.coreControlsEvent.emit(instance.UI.LayoutMode.Single);

      const { documentViewer, Annotations, annotationManager } = instance.Core;

      instance.UI.openElements(['notesPanel']);

      documentViewer.addEventListener('annotationsLoaded', () => {
        console.log('annotations loaded');
      });

      documentViewer.enableReadOnlyMode();
      documentViewer.addEventListener('documentLoaded', () => {
        this.documentLoaded$.next();
        const rectangleAnnot = new Annotations.RectangleAnnotation({
          PageNumber: 1,
          // values are in page coordinates with (0, 0) in the top left
          X: 100,
          Y: 150,
          Width: 200,
          Height: 50,
          Author: annotationManager.getCurrentUser()
        });
        annotationManager.addAnnotation(rectangleAnnot);
        annotationManager.redrawAnnotation(rectangleAnnot);
      });
      console.log('init');

    })
  }

  ngOnInit() {


  }

  loadDoc(){
    this.dataService.getFile(1,0).subscribe({
      next: data => {
        console.log(data);
        const blob = new Blob([data], { type: 'application/pdf' });
        const url= window.URL.createObjectURL(blob);
        this.wvInstance.Core.documentViewer.loadDocument(blob);
      },
      error: err => {
        console.log(err);
      }
    });
  }

  getDocumentLoadedObservable() {
    return this.documentLoaded$.asObservable();
  }
}