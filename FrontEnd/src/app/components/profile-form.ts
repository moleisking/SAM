import { Component, OnInit, Input } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder, FormControl } from "@angular/forms";
import { Validators } from "@angular/common";

import { UserService } from "../services/user";
import { UserDefaultImage } from "../config/userdefaultimage";
import { ProfileModel } from "../models/profile";

declare let google: any;

@Component({
    selector: "profile-form-component",
    templateUrl: "../../views/profile-form.html",
    styleUrls: ["../../styles/form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class ProfileFormComponent implements OnInit {

    @Input() model: ProfileModel = new ProfileModel();
    private image: string;

    private myForm: FormGroup;
    private defaultImage = UserDefaultImage.image;
    private message: string;

    constructor(private user: UserService, private formBuilder: FormBuilder) {
        this.message = "Profile form messages will be here.";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            description: [this.model.description, Validators.required],
            address: [this.model.address, Validators.required],
            mobile: [this.model.mobile, Validators.required],
            dayRate: [this.model.dayRate],
            hourRate: [this.model.hourRate],
            curLat: [this.model.curLat],
            curLng: [this.model.curLng]
        });
        this.image = this.model.image ? this.model.image : this.defaultImage;

        this.googleHook();
    }

    googleHook() {
        let searchBox: any = document.getElementById("address");
        let options = {
            types: ["geocode"], // return only geocoding results, rather than business results.
            componentRestrictions: { country: "es" }
        };

        let autocomplete = new google.maps.places.Autocomplete(searchBox, options);

        // Add listener to the place changed event
        autocomplete.addListener("place_changed", () => {
            this.getAddress(autocomplete.getPlace());
        });
    }

    getAddress(place: Object) {
        let address = place["formatted_address"];
        let location = place["geometry"]["location"];
        this.myForm.patchValue({curLat: location.lat()});
        this.myForm.patchValue({curLng: location.lng()});
        this.myForm.patchValue({address: address});
        console.log("place", address, location, location.lat(), location.lng());
    }

    ngAfterViewChecked() {
        if (document.getElementById("image") !== null)
            document.getElementById("image")
                .addEventListener("change", e => { this.readImage(e); }, false);
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
