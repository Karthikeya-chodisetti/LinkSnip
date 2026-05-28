import { useState } from "react";
import { Copy,QrCode } from "lucide-react";

export default function LinkCard({ link,refreshLinks }) {

    const [showQr,setShowQr]=useState(false);
    const [copied,setCopied]=useState(false);

    const shortUrl=`http://localhost:8080/api/links/${link.shortCode}`;

    const copyLink=async()=>{

        await navigator.clipboard.writeText(shortUrl);

        setCopied(true);

        setTimeout(()=>{
            setCopied(false);
        },1500);
    };

    return(
        <div className="bg-[#111827] border border-gray-700 rounded-2xl p-5 mb-5">

            <div className="flex justify-between items-start gap-4">

                <div className="flex-1 min-w-0">

                    <div className="flex items-center gap-3 flex-wrap">

                        <a
                            href={shortUrl}
                            target="_blank"
                            onClick={()=>{
                                setTimeout(()=>{
                                refreshLinks();
                            },500);
                            }}
                            className="text-blue-400 text-lg break-all hover:text-blue-300 hover:underline transition"
                        >
                            {shortUrl}
                        </a>

                        <button
                            onClick={()=>setShowQr(!showQr)}
                            className="text-yellow-400 hover:text-yellow-300 flex-shrink-0"
                        >
                            <QrCode size={20} />
                        </button>

                    </div>

                    <p className="text-gray-400 mt-3 break-all text-sm">
                        {link.originalUrl}
                    </p>

                    <div className="flex gap-6 mt-4 text-sm text-gray-300 flex-wrap">

                        <p>
                            Clicks: {link.clicks || 0}
                        </p>

                        <p>
                            Expiry: {
                                link.expiryAt
                                ? new Date(link.expiryAt).toLocaleString()
                                : "No Expiry"
                            }
                        </p>

                    </div>

                </div>

                <button
                    onClick={copyLink}
                    className="bg-blue-600 hover:bg-blue-700 p-2 rounded-lg flex-shrink-0"
                >
                    <Copy size={18} />
                </button>

            </div>

            {copied && (
                <p className="text-green-400 text-sm mt-3">
                    Copied to clipboard
                </p>
            )}

            {showQr && (
                <div className="mt-5 flex justify-center">

                    <img
                        src={`http://localhost:8080/api/links/qr/${link.shortCode}`}
                        alt="QR"
                        className="w-44 h-44 bg-white p-2 rounded-xl"
                    />

                </div>
            )}

        </div>
    );
}