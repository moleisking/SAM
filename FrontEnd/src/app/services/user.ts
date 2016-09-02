import { Injectable } from "@angular/core";
import { Http, Headers, Response, RequestOptions } from "@angular/http";
import { Settings } from "../config/settings";
import { UserModel } from "../models/user";
import { ProfileModel } from "../models/profile";
import { Observable } from "rxjs/Rx";

@Injectable()

export class UserService {

  constructor(private http: Http) { }

  register(user:any, lat:any, lng:any): Observable<any> {
    let body = "name=" + user.name + "&pass=" + user.pass + "&email=" + user.email
      + "&lat=" + lat + "&lng=" + lng + "&category=" + user.category + "&tags=" + user.tags;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/signup", body, options).catch(this.handleError);
  }

  saveProfile(profileform:any): Observable<any> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/X-www-form-urlencoded");
    let c = "description=" + profileform.description + "&name=" + profileform.name;

    return this.http.post(Settings.backend_url + "/users/saveprofile", c, { headers: headers }).catch(this.handleError);
  }

  all(): Observable<UserModel[]> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));

    return this.http.get(Settings.backend_url + "/users", { headers: headers })
      .map(this.extractData).catch(this.handleError);
  }

  getMyProfile(): Observable<ProfileModel> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));

    return this.http.get(Settings.backend_url + "/users/getmyprofile", { headers: headers })
      .map(this.extractData)
      .catch(this.handleError);
  }

  getProfile(id:any): Observable<ProfileModel> {
    return this.http.get(Settings.backend_url + "/users/getprofile/" + id)
      .map(this.extractData).catch(this.handleError);
  }

  forgottenpassword(form:any): Observable<any> {
    let creds = "email=" + form.email;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");

    return this.http.post(Settings.backend_url + "/forgottenpassword", creds, { headers: headers })
      .map(this.extractData).catch(this.handleError);
  }

  private extractData(res: Response) {
    // console.log(res);
    let body = res.json();
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