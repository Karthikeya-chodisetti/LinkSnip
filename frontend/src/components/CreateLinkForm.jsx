import { useState } from "react";
import API from "../services/api";

export default function CreateLinkForm({ refreshLinks }) {

    const [url,setUrl]=useState("");
    const [customAlias,setCustomAlias]=useState("");
    const [ttlValue,setTtlValue]=useState("");
    const [ttlUnit,setTtlUnit]=useState("DAYS");

    const [showAlias,setShowAlias]=useState(false);
    const [showExpiry,setShowExpiry]=useState(false);

    const handleCreate=async()=>{

        try{

            const token=localStorage.getItem("token");

            if(showExpiry && ttlValue){

                const value=Number(ttlValue);

                let totalMinutes=0;

                if(ttlUnit==="MINUTES") totalMinutes=value;

                if(ttlUnit==="HOURS") totalMinutes=value*60;

                if(ttlUnit==="DAYS") totalMinutes=value*1440;

                if(totalMinutes > 30*1440){

                    alert("Max expiry is 30 days");

                    return;
                }
            }

            const payload={
                url,
                customAlias:showAlias ? customAlias : null,
                ttlValue:showExpiry ? Number(ttlValue) : null,
                ttlUnit:showExpiry ? ttlUnit : null
            };

            await API.post("/api/links",payload,{
                headers:{
                    Authorization:`Bearer ${token}`
                }
            });

            setUrl("");
            setCustomAlias("");
            setTtlValue("");
            setShowAlias(false);
            setShowExpiry(false);

            refreshLinks();

        }catch(err){

            console.log(err);

            if(err.response?.status===429){

                alert("Too many requests. Please wait a minute and try again.");
                return;
            }
            alert(
                err.response?.data?.message || "Error creating link"
            );
        }
    };

    return(
        <div className="bg-[#111827] p-6 rounded-2xl border border-gray-700">

            <div className="flex gap-4 mb-6">

                <input
                    type="text"
                    placeholder="Paste your URL here..."
                    value={url}
                    onChange={(e)=>setUrl(e.target.value)}
                    className="flex-1 bg-[#0f172a] text-white border border-gray-700 rounded-xl px-4 py-3 outline-none"
                />

                <button
                    onClick={handleCreate}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-6 rounded-xl"
                >
                    Generate
                </button>

            </div>

            <div className="grid md:grid-cols-3 gap-4 mb-6">

                <button
                    onClick={()=>setShowAlias(!showAlias)}
                    className={`rounded-xl p-4 text-left border transition ${
                        showAlias
                        ? "border-blue-500 bg-[#1e293b]"
                        : "border-gray-700 bg-[#1e293b]"
                    }`}
                >
                    <h2 className="text-white font-semibold flex items-center justify-center cursor-pointer">
                        Custom Alias
                    </h2>
                </button>

                <button
                    onClick={()=>setShowExpiry(!showExpiry)}
                    className={`rounded-xl p-4 text-left border transition ${
                        showExpiry
                        ? "border-purple-500 bg-[#1e293b]"
                        : "border-gray-700 bg-[#1e293b]"
                    }`}
                >
                    <h2 className="text-white font-semibold flex items-center justify-center cursor-pointer">
                        Set Expiry
                    </h2>
                </button>

                <div className="bg-[#1e293b] rounded-xl border border-gray-700 hover:border-yellow-500 transition p-4 flex items-center justify-center cursor-pointer">

                    <h2 className="text-white font-semibold">
                        Generate QR
                    </h2>

                </div>

            </div>

            <div className="space-y-4">

                {showAlias && (
                    <div className="bg-[#1e293b] border border-gray-700 rounded-xl p-4">

                        <input
                            type="text"
                            placeholder="Enter custom alias"
                            value={customAlias}
                            onChange={(e)=>setCustomAlias(e.target.value)}
                            className="w-full bg-[#0f172a] text-white border border-gray-700 rounded-xl px-4 py-3 outline-none"
                        />

                    </div>
                )}

                {showExpiry && (
                    <div className="bg-[#1e293b] border border-gray-700 rounded-xl p-4 flex gap-4">

                        <input
                            type="number"
                            placeholder="TTL Value"
                            value={ttlValue}
                            onChange={(e)=>setTtlValue(e.target.value)}
                            className="flex-1 bg-[#0f172a] text-white border border-gray-700 rounded-xl px-4 py-3 outline-none appearance-none [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none"
                        />

                        <select
                            value={ttlUnit}
                            onChange={(e)=>setTtlUnit(e.target.value)}
                            className="bg-[#0f172a] text-white border border-gray-700 rounded-xl px-4 py-3"
                        >
                            <option value="MINUTES">Minutes</option>
                            <option value="HOURS">Hours</option>
                            <option value="DAYS">Days</option>
                        </select>

                    </div>
                )}

            </div>

        </div>
    );
}