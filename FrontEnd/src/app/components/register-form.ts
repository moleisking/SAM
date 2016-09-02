import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { CategoriesService } from "../services/categories";
import { CategoryModel } from "../models/category";
import { TagModel } from "../models/tag";

@Component({
    selector: "register-form-component",
    templateUrl: "../../views/register-form.html",
    styleUrls: ["../../styles/form.css", "../../styles/ng2-select.css"],
})

export class RegisterFormComponent implements OnInit {

    private myForm: FormGroup; // our model driven form
    private submitted: boolean; // keep track on form submission
    private message: string;
    private lat: number;
    private lng: number;
    private cats: Array<CategoryModel>;
    private tags: Array<TagModel>;
    private tagsValue: any = [];
    private areTagsAvailable: boolean = false;

    constructor(private auth: AuthService, private user: UserService, private router: Router,
        private formBuilder: FormBuilder, private cat: CategoriesService) {
        this.message = "Register messages will be here.";
        this.lat = 0;
        this.lng = 0;
    }

    ngOnInit() {
        let regexPatterns = { numbers: "^[0-9]*$" };
        this.myForm = this.formBuilder.group({
            name: ["", <any>Validators.required],
            pass: ["", [<any>Validators.required, <any>Validators.minLength(5), <any>Validators.maxLength(20)]],
            email: ["", <any>Validators.required],
            category: ["",
                Validators.compose([
                    Validators.pattern(regexPatterns.numbers),
                    Validators.required
                ])],
            tags: [""],
            address: ["", <any>Validators.required],
            mobile: [""]
        });
        this.getPosition();
        this.getCategories();
    }

    onChangeCategory(value:any) {
        this.tagsValue = []
        this.tags = this.cats.find(x => x.id === value).tags;
        this.areTagsAvailable = this.tags.length > 0;
    }

    getCategories() {
        this.cat.all().subscribe(
            c => { this.cats = c; },
            error => this.message = <any>error);
    }

    getPosition() {
        if (navigator.geolocation)
            navigator.geolocation.getCurrentPosition(position => { this.setPosition(position) }, this.showErrorGeoLoc);
        else
            this.message = "Geolocation is not supported by this browser or allowed. Can't register an user then.";
    }

    setPosition(position:any) {
        this.lat = position.coords.latitude;
        this.lng = position.coords.longitude;
    }

    register() {
        if (!this.lat || this.lat === undefined || this.lat === 0 ||
            !this.lng || this.lng === undefined || this.lng === 0)
            this.message = "Coordenades not specified. Can't register an user then.";
        else {
            this.submitted = true;
            this.myForm.controls["tags"].setValue(this.tagsValue.map((item: any) => { return item.id; }).join(","));
            this.message = "New user sent to be registered. Wait...";
            this.user.register(this.myForm.value, this.lat, this.lng).subscribe(
                () => this.router.navigate(["/login"]),
                error => this.message = <any>error,
                () => console.log("Done register call.")
            );
        }
    }

    showErrorGeoLoc(error:any) {
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

    public refreshValue(value: any): void {
        this.tagsValue = value;
    }

    // public selected(value: any): void {
    //     console.log("Selected value is: ", value);
    //     console.log(this.myForm.value);
    //     console.log(this.tagsValue);
    // }

    // public removed(value: any): void {
    //     console.log("Removed value is: ", value);
    // }

    // public typed(value: any): void {
    //     console.log("New input: ", value);
    // }

    // public itemsToString(value: Array<any> = []): string {
    //     return value
    //         .map((item: any) => {
    //             return item.text;
    //         }).join(",");
    // }
}