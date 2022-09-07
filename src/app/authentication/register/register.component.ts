import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthserviceService } from 'src/app/services/authservice.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  email: string= '';
  password: string= '';

  //Formulario de usuario
  public userForm: FormGroup;
  geo: any;
  user: any;
  constructor(
    private auth: AuthserviceService,
    private router: Router,
    public formBuilder: FormBuilder) {
  //Validaciones
    this.userForm= this.formBuilder.group({
      name: ['', Validators.required],
      mail: ['', Validators.required],
      password: new FormControl(this.password, [ Validators.required, Validators.minLength(8), Validators.pattern("^(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{8,100}$") ]),
      long: ['',Validators.required],
      lati: ['',Validators.required],
      id: null
    })
     }

  ngOnInit(): void {
    this.getGeolocation();
  }
  async register(){
    
    //Validacion de errores
    const res = await this.auth.register(this.userForm.value).catch(error =>{
      console.log("los datos del usuario son. ", this.userForm);
      alert("Registro incompleto")
      console.log('error', error);
    })
    if(res){
      console.log("Registro completo");
      const path = "users";
      const id= res.user.uid;
      this.userForm.patchValue({id: id});
    //Creando usuario 
      this.auth.createUser(this.userForm.value, path);
      
      this.auth.getObject(id, path).subscribe( res =>{
         this.user = res;
         localStorage.setItem("idUser", this.user.id);
         localStorage.setItem("nameUser", this.user.name);
         this.myAsyncFunction(); 
      })
      this.auth.sendEmailForVarification(res.user);
    }
  }
  async delay(n: number){
    return new Promise(function(resolve){
        setTimeout(resolve,n*1000);
    });
  }
  async  myAsyncFunction(){
    await this.delay(2);
  }
  redirect(){
    this.router.navigate(['/login']);
  }
  async getGeolocation(){
    this.auth.getlocation().then(resp=>{
      localStorage.setItem("longString", `${resp.long}`);
      localStorage.setItem("latiString", `${resp.lati}`);
      console.log("longitud: ", localStorage.getItem("longString"));
      console.log("latitud: ", localStorage.getItem("latiString"));
    })
    
    
  }
  //Metodo para crear geolocalización 
  async setGeolocation (){
    alert("Generar datos de Geolocalización");
    await this.delay(1);
    if(localStorage.getItem("longString")){
      this.userForm.patchValue({long: localStorage.getItem("longString")});
      this.userForm.patchValue({lati: localStorage.getItem("latiString")});
      alert("Datos generados");
    }
    else{
      alert("No fue posible obtener los datos");
    }
  }
  //Validaciones 
  emptyFields(field: string){
    if (this.userForm.get(field)?.hasError('required')) {
      return 'El campo es obligatorio';
    }
    return this.userForm.get(field)? 'Formato incorrecto' : '';
    }
  emptypassword(field: string){
    if (this.userForm.get(field)?.hasError('required')) {
      return 'El campo es obligatorio';
    }
    return this.userForm.get(field)? 'La contraseña debe tener más de 8 caracteres' : '';
    }
  get emptyName(){
  return this.userForm.get('name')?.invalid && this.userForm.get('name')?.touched
    }
  get emptyPassword(){
  return this.userForm.get('password')?.invalid && this.userForm.get('password')?.touched
    }
  get emptyemail(){
  return this.userForm.get('mail')?.invalid && this.userForm.get('mail')?.touched
    }

}
