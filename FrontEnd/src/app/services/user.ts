import { Injectable } from "@angular/core";
import { Http, Headers, Response, RequestOptions } from "@angular/http";
import { Observable } from "rxjs/Rx";

import { TranslateService } from "ng2-translate";

import { Settings } from "../config/settings";
import { UserModel } from "../models/user";
import { ProfileModel } from "../models/profile";
import { ChangePasswordModel } from "../models/changepassword";

@Injectable()
export class UserService {

  constructor(
    private http: Http,
    private trans: TranslateService
  ) { }

  register(user: UserModel, regLat: number, regLng: number): Observable<any> {
    let body =
      "name=" + user.name +
      "&password=" + user.password +
      "&email=" + user.email +
      "&regLat=" + regLat +
      "&regLng=" + regLng +
      "&category=" + user.category +
      "&tags=" + user.tags +
      "&address=" + user.address +
      "&mobile=" + user.mobile +
      "&username=" + user.username;

    let headers = new Headers();
    headers.append("Content-Type", "application/x-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/signup?locale=" + this.trans.currentLang,
      body, options).catch(this.handleError);
  }

  saveProfile(profileform: ProfileModel, image: string): Observable<any> {
    let body =
      "description=" + profileform.description +
      "&address=" + profileform.address +
      "&mobile=" + profileform.mobile +
      "&image=" + image +
      "&dayRate=" + profileform.dayRate +
      "&hourRate=" + profileform.hourRate +
      "&curLat=" + profileform.curLat +
      "&curLng=" + profileform.curLng +
      "&category=" + profileform.category +
      "&tags=" + profileform.tags +
      "&available=" + profileform.available;

    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/x-www-form-urlencoded");

    return this.http.post(Settings.backend_url + "/users/saveprofile?locale=" + this.trans.currentLang, body,
      { headers: headers }).catch(this.handleError);
  }

  all(): Observable<UserModel[]> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    let options = new RequestOptions({ headers: headers });

    return this.http.get(Settings.backend_url + "/users?locale=" + this.trans.currentLang, options)
      .map((res: Response) => res.json().all).catch(this.handleError);
  }

  getMyProfile(): Observable<ProfileModel> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    let options = new RequestOptions({ headers: headers });

    return this.http.get(Settings.backend_url + "/users/getmyprofile?locale=" + this.trans.currentLang, options)
      .map((res: Response) => res.json().myuser).catch(this.handleError);
  }

  getProfile(id: string): Observable<ProfileModel> {
    return this.http.get(Settings.backend_url + "/users/getprofile/" + id + "?locale=" + this.trans.currentLang)
      .map((res: Response) => res.json().profile).catch(this.handleError).cache();
  }

  forgottenpassword(form: any): Observable<any> {
    let creds = "email=" + form.email;
    let headers = new Headers();
    headers.append("Content-Type", "application/x-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/forgottenpassword?locale=" + this.trans.currentLang, creds, options)
      .map(this.extractData).catch(this.handleError);
  }

  search(regLat: number, regLng: number, category: number, radius: number): Observable<UserModel[]> {
    let body = "regLat=" + regLat + "&regLng=" + regLng + "&category=" + category + "&radius=" + radius;
    let headers = new Headers();
    headers.append("Content-Type", "application/x-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/users/search?locale=" + this.trans.currentLang, body, options)
      .map((res: Response) => res.json().users).catch(this.handleError);
  }

  addCode(code: string): Observable<any> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/x-www-form-urlencoded");

    let options = new RequestOptions({ headers: headers });
    let body = "code=" + code.trim();

    return this.http.post(Settings.backend_url + "/users/activate?locale=" + this.trans.currentLang, body, options)
      .map((res: Response) => res.json().activate).catch(this.handleError);
  }

  resendCode(): Observable<any> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/x-www-form-urlencoded");

    let options = new RequestOptions({ headers: headers });

    return this.http.get(Settings.backend_url + "/users/resendcode?locale=" + this.trans.currentLang, options)
      .map((res: Response) => res.json().resendcode).catch(this.handleError);
  }

  changePassword(passwordForm: ChangePasswordModel): Observable<any> {
    console.log(passwordForm)
    let body =
      "oldpassword=" + passwordForm.oldpassword +
      "&newpassword=" + passwordForm.newpassword +
      "&confirmpassword=" + passwordForm.confirmpassword;

    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/x-www-form-urlencoded");

    return this.http.post(Settings.backend_url + "/changepassword?locale=" + this.trans.currentLang, body,
      { headers: headers }).catch(this.handleError);
  }

  private extractData(res: Response) {
    // console.log(res);
    let body = res.json();
    // console.log(body.data);
    return body.data || {};
  }

  private handleError(error: any) {
    // console.error(error);
    let errMsg = (error.message) ? error.message :
      (error._body) ? error._body :
        error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
