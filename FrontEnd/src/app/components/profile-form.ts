import { Component, OnInit, Input } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Settings } from "../config/settings";

import { UserService } from "../services/user";
import { TranslateService } from "ng2-translate";

import { SelectComponent } from "ng2-select";

import { UserDefaultImage } from "../config/userdefaultimage";
import { ProfileModel } from "../models/profile";
import { CategoryModel } from "../models/category";

declare let google: any;

@Component({
    selector: "profile-form-component",
    templateUrl: "../../views/profile-form.html",
    styleUrls: ["../../styles/form.css", "../../styles/ng2-select.css"]
})

export class ProfileFormComponent implements OnInit {

    @Input() model: ProfileModel;
    @Input() catAll: CategoryModel[];

    private image: string;

    private myForm: FormGroup;
    private defaultImage = UserDefaultImage.image;
    private message: string;

    private tagsActive: any = [];

    constructor(
        private user: UserService,
        private formBuilder: FormBuilder,
        private trans: TranslateService
    ) { this.message = ""; } // Profile form messages will be here.

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            description: [this.model.description, Validators.required],
            address: [this.model.address, Validators.required],
            mobile: [this.model.mobile, Validators.required],
            dayRate: [this.model.dayRate],
            hourRate: [this.model.hourRate],
            curLat: [this.model.curLat],
            curLng: [this.model.curLng],
            available: [this.model.available]
        });

        this.image = this.model.image ? this.model.image : this.defaultImage;

        this.model.tags.split(",").forEach(element => {
            if (element)
                this.tagsActive.push(this.catAll.find(e => e.id.toString() === element));
        });

        this.googleHook();
    }

    refreshItems(value: number): void {
        this.tagsActive = value;
    }

    googleHook() {
        let searchBox: any = document.getElementById("address");
        let options = {
            types: ["geocode"], // return only geocoding results, rather than business results.
            componentRestrictions: { country: Settings.search_country }
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
        this.myForm.patchValue({ curLat: location.lat() });
        this.myForm.patchValue({ curLng: location.lng() });
        this.myForm.patchValue({ address: address });
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
        console.log(this.tagsActive.length)
        if ((!this.myForm.dirty && !this.myForm.valid) || this.tagsActive.length === 0)
            this.trans.get("FormNotValid").subscribe((res: string) => this.message = res);
        else {
            this.trans.get("UserProfileSent").subscribe((res: string) => this.message = res);
            this.user.saveProfile(this.myForm.value, this.image, this.tagsActive).subscribe(
                () => this.trans.get("UserProfileSaved").subscribe((res: string) => this.message = res),
                (res) => this.message = res
            );
        }
    }
}
