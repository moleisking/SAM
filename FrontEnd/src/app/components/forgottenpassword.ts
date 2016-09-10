import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { UserService } from "../services/user";

@Component({
    selector: "forgottenpassword-form-component",
    templateUrl: "../../views/forgottenpassword.html",
    styleUrls: ["../../styles/form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES],
    providers: [UserService]
})

export class ForgottenPassword implements OnInit {

    private myForm: FormGroup;
    private message: string;
    private submitted: boolean; // keep track on form submission

    constructor(private builder: FormBuilder, private formBuilder: FormBuilder, private user: UserService) {
        this.message = "Forgotten Password Text";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            email: ["", Validators.required]
        });
    }

    send() {
        if (!this.myForm.dirty || !this.myForm.valid)
            this.message = "Form not valid to be sent.";
        else {
            this.submitted = true;
            this.user.forgottenpassword(this.myForm.value).subscribe(
                data => {
                    this.message = data.message;
                },
                error => {
                    this.message = "Error sending email.";
                },
                () => console.log("Done forgotten password")
            );
        }
    }
}
