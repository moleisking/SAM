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

    private message: string;
    private description: string;
    private mobile: string;
    private address: string;
    private image: string;

    constructor(private user: UserService, private formBuilder: FormBuilder) {
        this.message = "Profile form messages will be here.";
    }

    ngOnInit() {
        this.getMyProfile();
        this.myForm = this.formBuilder.group({
            description: [this.description, Validators.required],
            address: [this.address, Validators.required],
            image: [this.image],
            mobile: [this.mobile]
        });
    }

    ngAfterViewChecked() {
        if (document.getElementById("image") !== null)
            document.getElementById("image")
                .addEventListener("change", e => { this.readImage(e); }, false);
    }

    getMyProfile() {
        this.user.getMyProfile().subscribe(
            profile => {
                this.image = profile.image;
                this.description = profile.description;
                this.mobile = profile.mobile;
                this.address = profile.address;
            },
            error => this.message = <any>error);
    }

    readImage(e: any) {
        let fileName = e.target.files[0];
        if (!fileName)
            return;
        let reader = new FileReader();
        let self = this;
        reader.onload = file => {
            let contents: any = file.target;
            self.image = contents.result;
        };
        reader.readAsDataURL(fileName);
    }

    save() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.message = "Form not valid to be sent.";
        else {
            this.message = "User profile sent...";
            this.user.saveProfile(this.myForm.value, this.image).subscribe(
                () => {
                    this.message = "User profile saved.";
                },
                (res) => {
                    this.message = res;
                }
            );
        }
    }
}
