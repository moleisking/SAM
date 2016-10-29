import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { WebService } from "../services/web";
import { TranslateService } from "ng2-translate";

@Component({
    selector: "contactus-form-component",
    providers: [WebService],
    templateUrl: "../../views/contactus-form.html",
    styleUrls: ["../../styles/form.css"]
})

export class ContactUsFormComponent implements OnInit {

    private myForm: FormGroup;

    private message: string;
    private description: string;

    constructor(
        private web: WebService,
        private formBuilder: FormBuilder,
        private trans: TranslateService
    ) {
        this.message = ""; // Contact Us form messages will be here.
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            name: ["", Validators.required],
            surname: ["", Validators.required],
            email: ["", Validators.required],
            notification: ["", Validators.required]
        });
    }

    sendContactForm() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.trans.get("FormNotValid").subscribe((res: string) => this.message = res);
        else {
            this.trans.get("MessagesContactUsSending").subscribe((res: string) => this.message = res);
            this.web.sendContactForm(this.myForm.value).subscribe(
                data => this.trans.get("MessagesSent").subscribe((res: string) => this.message = res),
                error => this.trans.get("MessagesSentContactUsError").subscribe((res: string) => this.message = res),
                () => this.trans.get("DoneContactUs").subscribe((res: string) => console.log(res))
            );
        }
    }
}
