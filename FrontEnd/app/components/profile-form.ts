import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { UserService } from "../services/user";

@Component({
    selector: "profile-form-component",
    providers: [UserService],
    templateUrl: "/views/profile-form.html",
    styleUrls: ["/styles/profile-form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class ProfileFormComponent implements OnInit {

    public myForm: FormGroup; // our model driven form
    public submitted: boolean; // keep track on form submission
    public message: string;
    public description: string;

    constructor(private user: UserService, private formBuilder: FormBuilder) {
        this.message = "Profile form messages will be here.";
    }

    ngOnInit() {
        this.getLoggedProfile();
        this.myForm = this.formBuilder.group({
            name: [localStorage.getItem("auth_key_name"), <any>Validators.required],
            description: [this.description, <any>Validators.required]
        });
    }

    getLoggedProfile() {
        this.user.getLoggedProfile().subscribe(
            profile => this.description = profile.description,
            error => this.message = <any>error);
    }

    save() {
        this.submitted = true;
        this.message = "User profile sent.";
        this.user.saveProfile(this.myForm.value).then(
            () => {
                this.message = "User profile saved.";
            },
            (res) => {
                this.message = res;
            }
        );
    }
}