import { Component } from "@angular/core";
import { ContactUsFormComponent } from "./contactus-form";

@Component({
    selector: "contactus-component",
    templateUrl: "/views/contactus.html",
    directives: [ ContactUsFormComponent ]
})

export class ContactUs { }