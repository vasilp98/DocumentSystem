import {Component, ViewChild, OnInit, Output, EventEmitter, ElementRef, OnDestroy} from '@angular/core';
import WebViewer,  { WebViewerInstance } from '@pdftron/webviewer';
import getInstance from '@pdftron/webviewer';
import {DataService} from "@core/services/data.service";
import {Subject} from "rxjs";
import { MessageService } from "@core/services/message.service";

@Component({
  selector: 'app-viewer',
  templateUrl: './viewer.component.html',
  styleUrls: ['./viewer.component.scss']
})
export class ViewerComponent implements OnInit, OnDestroy {
  selectedDocument;
  @ViewChild('viewer') viewer: ElementRef;
  wvInstance: WebViewerInstance;
  @Output() coreControlsEvent:EventEmitter<string> = new EventEmitter();
  private documentLoaded$: Subject<void>;

  constructor(private dataService: DataService,private messageService: MessageService) {
    this.documentLoaded$ = new Subject<void>();
    this.messageService.currentMessage.subscribe(message => {
      this.selectedDocument = message;
      setTimeout(() => {
        this.loadViewer();
      });
    });
  }

  loadViewer() {
    WebViewer({
      path: '../../../lib'
    }, this.viewer.nativeElement).then(instance => {
      this.wvInstance = instance;
      this.coreControlsEvent.emit(instance.UI.LayoutMode.Single);
      const { documentViewer, Annotations, annotationManager } = instance.Core;
      instance.UI.openElements(['notesPanel']);
      documentViewer.zoomTo(100);
      documentViewer.enableReadOnlyMode();
      this.loadFile();
    })
  }
  ngOnInit(): void {

  }

  ngOnDestroy(): void {
  }


  getDocumentLoadedObservable() {
    return this.documentLoaded$.asObservable();
  }

  loadFile(){
    this.dataService.getFile(this.selectedDocument,0).subscribe({
      next: data => {
        const blob = new Blob([data], { type: 'application/pdf' });
        setTimeout(() => {
          this.wvInstance.Core.documentViewer.loadDocument(blob, {extension:"pdf"});
        });
      },
      error: err => {
        console.log(err);
      }
    });
  }

}
