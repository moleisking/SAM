import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { WebService } from "../services/web";

@Component({
    selector: "contactus-form-component",
    providers: [WebService],
    templateUrl: "/views/contactus-form.html",
    styleUrls: ["/styles/contactus-form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class ContactUsFormComponent implements OnInit {

    public myForm: FormGroup; // our model driven form
    public submitted: boolean; // keep track on form submission
    public message: string;
    public description: string;

    constructor(private web: WebService, private router: Router, private formBuilder: FormBuilder) {
        this.message = "Contact Us form messages will be here.";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            email: ["", <any>Validators.required],
            message: ["", <any>Validators.required]
        });
    }

    sendContactForm() {
        this.submitted = true;
        this.message = "Contact Us message sent.";
        this.web.sendContactForm(this.myForm.value)
            .subscribe(
            data => {
                this.message = data.message;
            },
            error => {
                this.message = "Error sending Contact Us form.";
            },
            () => console.log("Done Contact Us")
            );
    }
}