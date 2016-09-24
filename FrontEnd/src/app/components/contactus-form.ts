import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { WebService } from "../services/web";

@Component({
    selector: "contactus-form-component",
    providers: [WebService],
    templateUrl: "../../views/contactus-form.html",
    styleUrls: ["../../styles/form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class ContactUsFormComponent implements OnInit {

    private myForm: FormGroup;

    private message: string;
    private description: string;

    constructor(private web: WebService, private formBuilder: FormBuilder) {
        this.message = "Contact Us form messages will be here.";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            email: ["", Validators.required],
            notification: ["", Validators.required]
        });
    }

    sendContactForm() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.message = "Form not valid to be sent.";
        else {

            this.message = "Contact Us message sending...";
            this.web.sendContactForm(this.myForm.value).subscribe(
                data => {
                    this.message = "Message sent.";
                },
                error => {
                    this.message = "Error sending Contact Us form.";
                },
                () => console.log("Done Contact Us")
            );
        }
    }
}
