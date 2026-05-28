import { useState } from "react";
import { Eye, EyeOff } from "lucide-react";
import API from "../services/api";

export default function Register(){

    const [username,setUsername]=useState("");
    const [email,setEmail]=useState("");
    const [password,setPassword]=useState("");

    const [showPassword,setShowPassword]=useState(false);

    const [error,setError]=useState("");

    const handleRegister=async(e)=>{

        e.preventDefault();

        setError("");

        try{

            await API.post("/api/auth/register",{
                username,
                email,
                password
            });

            alert("Registered Successfully");

            window.location.href="/";

        }catch(err){

            setError(
                err.response?.data?.message || 
                "Registration failed"
            );
        }
    };

    return(

        <div className="min-h-screen bg-gradient-to-br from-blue-100 via-sky-100 to-cyan-100 flex items-center justify-center px-6">

            <div className="w-full max-w-6xl grid md:grid-cols-2 rounded-3xl overflow-hidden shadow-2xl border border-blue-200">

                <div className="hidden md:flex flex-col justify-center bg-gradient-to-br from-sky-500 to-blue-700 p-12">

                    <h1 className="text-5xl font-bold text-white mb-6">
                        LinkSnip
                    </h1>

                    <p className="text-blue-100 text-lg leading-8">
                        Create short links, custom aliases,
                        expiry-based URLs and downloadable QR codes
                        with secure JWT authentication.
                    </p>

                </div>

                <div className="bg-white p-10 md:p-14">

                    <h2 className="text-4xl font-bold text-gray-800 mb-2">
                        Create Account
                    </h2>

                    <p className="text-gray-500 mb-10">
                        Start managing your smart links
                    </p>

                    <form
                        onSubmit={handleRegister}
                        className="space-y-6"
                    >

                        <div>

                            <label className="text-gray-700 text-sm">
                                Username
                            </label>

                            <input
                                type="text"
                                placeholder="Enter username"
                                value={username}
                                onChange={(e)=>setUsername(e.target.value)}
                                className="w-full mt-2 bg-blue-50 text-gray-800 border border-blue-200 rounded-xl px-4 py-4 outline-none focus:border-blue-500"
                            />

                        </div>

                        <div>

                            <label className="text-gray-700 text-sm">
                                Email
                            </label>

                            <input
                                type="email"
                                placeholder="Enter email"
                                value={email}
                                onChange={(e)=>setEmail(e.target.value)}
                                className="w-full mt-2 bg-blue-50 text-gray-800 border border-blue-200 rounded-xl px-4 py-4 outline-none focus:border-blue-500"
                            />

                        </div>

                        <div>

                            <label className="text-gray-700 text-sm">
                                Password
                            </label>

                            <div className="relative mt-2">

                                <input
                                    type={showPassword ? "text" : "password"}
                                    placeholder="Enter password"
                                    value={password}
                                    onChange={(e)=>setPassword(e.target.value)}
                                    className="w-full bg-blue-50 text-gray-800 border border-blue-200 rounded-xl px-4 py-4 pr-14 outline-none focus:border-blue-500"
                                />

                                <button
                                    type="button"
                                    onClick={()=>setShowPassword(!showPassword)}
                                    className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500"
                                >
                                    {
                                        showPassword
                                        ? <EyeOff size={22}/>
                                        : <Eye size={22}/>
                                    }
                                </button>

                            </div>

                        </div>

                        {error && (

                            <p className="text-red-500 text-sm text-center">
                                {error}
                            </p>
                        )}

                        <button
                            type="submit"
                            className="w-full bg-blue-600 hover:bg-blue-700 transition-all text-white py-4 rounded-xl font-semibold text-lg shadow-md"
                        >
                            Register
                        </button>

                    </form>

                    <p className="text-gray-500 mt-8 text-center">

                        Already have an account?

                        <span
                            onClick={()=>window.location.href="/login"}
                            className="text-blue-600 cursor-pointer ml-2 font-medium hover:underline"
                        >
                            Login
                        </span>

                    </p>

                </div>

            </div>

        </div>
    );
}