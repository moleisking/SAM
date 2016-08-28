import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { AuthService } from "../services/auth";
import { UserService } from "../services/user";

@Component({
    selector: "register-form-component",
    templateUrl: "/views/register-form.html",
    styleUrls: ["/styles/form.css"],
})

export class RegisterFormComponent implements OnInit {

    private myForm: FormGroup; // our model driven form
    private submitted: boolean; // keep track on form submission
    private message: string;
    private lat: number;
    private lng: number;

    constructor(private auth: AuthService, private user: UserService, private router: Router, private formBuilder: FormBuilder) {
        this.message = "Register messages will be here.";
        this.lat = 0;
        this.lng = 0;
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            name: ["", <any>Validators.required],
            pass: ["", [<any>Validators.required, <any>Validators.minLength(5), <any>Validators.maxLength(20)]],
            email: ["", <any>Validators.required]
        });
        this.getPosition();
    }

    getPosition() {
        if (navigator.geolocation)
            navigator.geolocation.getCurrentPosition(position => { this.setPosition(position) }, this.showErrorGeoLoc);
        else
            this.message = "Geolocation is not supported by this browser or allowed. Can't register an user then.";
    }

    setPosition(position) {
        this.lat = position.coords.latitude;
        this.lng = position.coords.longitude;
    }

    register() {
        if (!this.lat || this.lat === undefined || this.lat === 0 ||
            !this.lng || this.lng === undefined || this.lng === 0)
            this.message = "Coordenades not specified. Can't register an user then.";
        else {
            this.submitted = true;
            this.message = "New user sent to be registered. Wait...";
            this.user.register(this.myForm.value).subscribe(
                () => this.router.navigate(["/login"]),
                error => this.message = <any>error,
                () => console.log("Done register call.")
            );
        }
    }

    showErrorGeoLoc(error) {
        switch (error.code) {
            case error.PERMISSION_DENIED:
                this.message = "User denied the request for Geo-location.";
                break;
            case error.POSITION_UNAVAILABLE:
                this.message = "Location information is unavailable.";
                break;
            case error.TIMEOUT:
                this.message = "The request to get user location timed out.";
                break;
            case error.UNKNOWN_ERROR:
                this.message = "An unknown error occurred.";
                break;
        }
    }
}