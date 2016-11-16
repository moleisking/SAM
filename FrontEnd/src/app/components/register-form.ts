import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { Settings } from "../config/settings";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { CategoriesService } from "../services/categories";
import { TranslateService } from "ng2-translate";

import { CategoryModel } from "../models/category";

declare let google: any;

@Component({
    selector: "register-form-component",
    templateUrl: "../../views/register-form.html",
    styleUrls: ["../../styles/form.css"]
})

export class RegisterFormComponent implements OnInit {

    private myForm: FormGroup;

    private message: string;
    private regLat: number;
    private regLng: number;
    private cats: Array<CategoryModel>;
    private isGoogleVisible: boolean = false;

    constructor(
        private auth: AuthService,
        private user: UserService,
        private router: Router,
        private formBuilder: FormBuilder,
        private cat: CategoriesService,
        private trans: TranslateService
    ) {
        this.message = ""; // Register messages will be here.
        this.regLat = 0;
        this.regLng = 0;
    }

    ngOnInit() {
        let searchBox: any = document.getElementById("address");
        let options = {
            // return only geocoding results, rather than business results.
            types: ["geocode"], componentRestrictions: { country: Settings.search_country }
        };

        let autocomplete = new google.maps.places.Autocomplete(searchBox, options);

        // Add listener to the place changed event
        autocomplete.addListener("place_changed", () => {
            let place = autocomplete.getPlace();
            let address = place.formatted_address;
            this.regLat = place.geometry.location.lat();
            this.regLng = place.geometry.location.lng();
            this.myForm.patchValue({ address: address });
        });

        let regexPatterns = { numbers: "^[0-9]*$" };
        this.myForm = this.formBuilder.group({
            username: ["", Validators.required],
            name: ["", Validators.required],
            password: ["", [Validators.required, Validators.minLength(5)]],
            email: ["", Validators.required],
            category: ["", Validators.compose([Validators.pattern(regexPatterns.numbers), Validators.required])],
            address: ["", Validators.required],
            mobile: [""]
        });

        this.getPosition();
        this.getCategories();
    }

    getCategories() {
        this.cat.all().subscribe(
            c => this.cats = c,
            error => this.message = <any>error
        );
    }

    getPosition() {
        if (navigator.geolocation)
            navigator.geolocation.getCurrentPosition(
                position => this.setPosition(position),
                error => this.showErrorGeoLoc(error)
            );
        else {
            this.trans.get("GeoNotAllowed").subscribe((res: string) => this.message = res);
            this.isGoogleVisible = true;
        }
    }

    setPosition(position: any) {
        this.regLat = position.coords.latitude;
        this.regLng = position.coords.longitude;
    }

    showErrorGeoLoc(error: any) {
        this.trans.get("WillAskGoogle").subscribe((res: string) => this.message = error.message + ", " + res);
        this.isGoogleVisible = true;
    }

    register() {
        if (!this.regLat || this.regLat === undefined || this.regLat === 0 ||
            !this.regLng || this.regLng === undefined || this.regLng === 0)
            this.trans.get("NoCoordenades").subscribe((res: string) => this.message = res);
        else {
            if (!this.myForm.dirty && !this.myForm.valid)
                this.trans.get("FormNotValid").subscribe((res: string) => this.message = res);
            else {
                this.trans.get("NewUserSent").subscribe((res: string) => this.message = res);
                this.user.register(this.myForm.value, this.regLat, this.regLng).subscribe(
                    () => this.router.navigate(["/login"]),
                    error => this.message = <any>error,
                    () => this.trans.get("DoneRegisterCall").subscribe((res: string) => console.log(res))
                );
            }
        }
    }
}
