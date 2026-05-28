import { useEffect,useState } from "react";
import API from "../services/api";
import CreateLinkForm from "../components/CreateLinkForm";
import LinkCard from "../components/LinkCard";

export default function Dashboard(){

    const [links,setLinks]=useState([]);
    const [username,setUsername]=useState("");

    const fetchLinks=async()=>{

        try{

            const token=localStorage.getItem("token");

            if(!token){
                window.location.href="/";
                return;
            }

            const payload=JSON.parse(atob(token.split(".")[1]));

            const email=payload.sub;

            const name=email.split("@")[0];

            setUsername(name);

            const res=await API.get("/api/links/my",{
                headers:{
                    Authorization:`Bearer ${token}`
                }
            });

            setLinks([...res.data].reverse());

        }catch(err){

            console.log(err);

            if(err.response?.status===401 || err.response?.status===403){

                localStorage.removeItem("token");

                window.location.href="/";
            }
        }
    };

    useEffect(()=>{
        fetchLinks();
    },[]);

    const logout=()=>{

        localStorage.removeItem("token");

        window.location.href="/";
    };

    return(
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-800 text-white p-6">
            
            <div className="max-w-5xl mx-auto backdrop-blur-sm">

                <div className="flex justify-between items-center mb-8">

                    <div>

                        <p className="text-green-300 mt-2 text-3xl font-bold">
                            Hello {username}
                        </p>

                    </div>

                    <button
                        onClick={logout}
                        className="bg-red-600 hover:bg-red-700 px-4 py-2 rounded-lg"
                    >
                        Logout
                    </button>

                </div>

                <CreateLinkForm refreshLinks={fetchLinks} />

                <div className="mt-10">

                    <h2 className="text-2xl font-semibold mb-6">
                        Your Links
                    </h2>

                    {links.length===0 && (

                        <div className="bg-[#111827]/80 border border-gray-700 rounded-3xl p-10 text-center mb-8">

                            <h3 className="text-3xl font-bold text-white mb-4">
                                No Links Yet
                            </h3>

                            <p className="text-gray-400 text-lg leading-8 max-w-2xl mx-auto">
                                Paste your long URL above and instantly create
                                powerful short links. Add custom aliases,
                                set expiry times, track click analytics and
                                generate QR codes for seamless sharing.
                            </p>

                        </div>
                    )}

                    {links.map((link)=>(
                        <LinkCard
                            key={link.id}
                            link={link}
                            refreshLinks={fetchLinks}
                        />
                    ))}

                </div>

                <div className="mt-16 bg-gradient-to-r from-[#111827] to-[#1e293b] border border-gray-700 rounded-3xl p-10">

                    <h2 className="text-3xl font-bold mb-6">
                        Smart Links. Smarter Sharing.
                    </h2>

                    <div className="grid md:grid-cols-3 gap-8">

                        <div>

                            <h3 className="text-xl font-semibold text-blue-400 mb-3">
                                Custom Aliases
                            </h3>

                            <p className="text-gray-400 leading-7">
                                Create memorable short links using personalized
                                aliases instead of random random characters.
                            </p>

                        </div>

                        <div>

                            <h3 className="text-xl font-semibold text-purple-400 mb-3">
                                Expiry Control
                            </h3>

                            <p className="text-gray-400 leading-7">
                                Set automatic expiry times for temporary
                                or secure links with minute, hour and day precision.
                            </p>

                        </div>

                        <div>

                            <h3 className="text-xl font-semibold text-yellow-400 mb-3">
                                QR Sharing
                            </h3>

                            <p className="text-gray-400 leading-7">
                                Generate QR codes instantly for every short
                                link and share them across devices effortlessly.
                            </p>

                        </div>

                    </div>

                </div>

            </div>

        </div>
    );
}