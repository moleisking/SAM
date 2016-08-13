import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { WebService } from "../services/web";

@Component({
    selector: "contactus-form-component",
    providers: [WebService],
    templateUrl: "/views/contactus-form.html",
    styleUrls: ["/styles/form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class ContactUsFormComponent implements OnInit {

    private myForm: FormGroup; // our model driven form
    private submitted: boolean; // keep track on form submission
    private message: string;
    private description: string;

    constructor(private web: WebService, private formBuilder: FormBuilder) {
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