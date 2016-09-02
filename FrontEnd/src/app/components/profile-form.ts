import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { UserService } from "../services/user";

@Component({
    selector: "profile-form-component",
    providers: [UserService],
    templateUrl: "../../views/profile-form.html",
    styleUrls: ["../../styles/form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class ProfileFormComponent implements OnInit {

    private myForm: FormGroup; // our model driven form
    private submitted: boolean; // keep track on form submission
    private message: string;
    private description: string;

    constructor(private user: UserService, private formBuilder: FormBuilder) {
        this.message = "Profile form messages will be here.";
    }

    ngOnInit() {
        this.getMyProfile();
        this.myForm = this.formBuilder.group({
            description: [this.description, <any>Validators.required]
        });
    }

    getMyProfile() {
        this.user.getMyProfile().subscribe(
            profile => this.description = profile.description,
            error => this.message = <any>error);
    }

    save() {
        this.submitted = true;
        this.message = "User profile sent.";
        this.user.saveProfile(this.myForm.value).subscribe(
            () => {
                this.message = "User profile saved.";
            },
            (res) => {
                this.message = res;
            }
        );
    }
}