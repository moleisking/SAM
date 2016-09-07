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
    private mobile: string;
    private address: string;
    private imageBase64: string;
    private imageCode: string;

    constructor(private user: UserService, private formBuilder: FormBuilder) {
        this.message = "Profile form messages will be here.";
    }

    ngOnInit() {
        this.getMyProfile();
        this.myForm = this.formBuilder.group({
            description: [this.description, <any>Validators.required],
            address: [this.address, <any>Validators.required],
            mobile: [this.mobile]
        });
    }

    ngAfterViewChecked() {
        if (document.getElementById("image") !== null)
            document.getElementById("image").addEventListener("change", e => { this.readImage(e); }, false);
    }

    getMyProfile() {
        this.user.getMyProfile().subscribe(
            profile => {
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
            self.imageBase64 = contents.result.substring(0, contents.result.indexOf("base64,") + 7);
            self.imageCode = contents.result.substring(contents.result.indexOf("base64,") + 7);
            console.log(self.imageCode)
        };
        reader.readAsDataURL(fileName);
    }

    save() {
        this.submitted = true;
        this.message = "User profile sent...";
        this.user.saveProfile(this.myForm.value, this.imageBase64, this.imageCode).subscribe(
            () => {
                this.message = "User profile saved.";
            },
            (res) => {
                this.message = res;
            }
        );
    }
}
