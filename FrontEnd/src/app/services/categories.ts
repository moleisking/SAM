import { Injectable } from "@angular/core";
import { Http, Headers, Response } from "@angular/http";
import { Observable } from "rxjs/Rx";

import { TranslateService } from "ng2-translate";

import { Settings } from "../config/settings";
import { CategoryModel } from "../models/category";
import { ProfileModel } from "../models/profile";

@Injectable()
export class CategoriesService {

  constructor(
    private http: Http,
    private trans: TranslateService
  ) { }

  all(): Observable<CategoryModel[]> {
    return this.http.get(Settings.backend_url + "/categories?locale=" + this.trans.currentLang)
      .map((res: Response) => res.json().categories)
      .map((x: any) => {
        return x.map((y: any) => {
          y.text = y.text[0].toUpperCase() + y.text.substr(1);
          y.description = y.description[0].toUpperCase() + y.description.substr(1);
          return y;
        });
      })
      .catch(this.handleError);
  }

  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}
