import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { UserService } from "../services/user";

@Component({
    selector: "forgottenpassword-form-component",
    templateUrl: "../../views/forgottenpassword.html",
    styleUrls: ["../../styles/form.css"]
})

export class ForgottenPassword implements OnInit {

    private myForm: FormGroup;
    private message: string;


    constructor(private builder: FormBuilder, private user: UserService) {
        this.message = "Forgotten Password Text";
    }

    ngOnInit() {
        this.myForm = this.builder.group({
            email: ["", Validators.required]
        });
    }

    send() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.message = "Form not valid to be sent.";
        else {

            this.user.forgottenpassword(this.myForm.value).subscribe(
                data => {
                    this.message = "Petition send.";
                },
                error => {
                    this.message = "Error sending email.";
                },
                () => console.log("Done forgotten password")
            );
        }
    }
}
