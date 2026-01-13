import axios from "axios";
import { createContext, useContext, useEffect, useState } from "react";
import { useAuth } from "./AuthContext";

const gigContext = createContext();
export const useGig=()=>{
    const value = useContext(gigContext);
    return value;
}
// provider part
function CustomGigContext({children}){
    const {user,token} = useAuth();
    const [gigs,setGigs] = useState([]);
    const [search,setSearch] = useState("");
    const [category,setCategory] = useState("");

    useEffect(() => {
        if(!user||!token) return ;

        const fetchGig = async()=>{
            try{
                const url = user.role==="CLIENT"
                ?"http://localhost:8080/api/gigs"
                :"http://localhost:8080/api/gigs/me";

                const res = await axios.get(url,{
                    headers:{
                        Authorization: `Bearer ${token}`
                    }
                });
                setGigs(res.data)
            }
            catch(err){
                console.error("Failed to fetch gigs",err);
            }
        }
        fetchGig();
    }, [user,token]);

    // Filtered gigs
    const filteredGigs = gigs.filter(gig =>
        (gig.title || "").toLowerCase().includes(search.toLowerCase()) &&
        (category ? gig.category === category : true)
    );

    const addGig = (newGig) => {
    setGigs((prev) => [...prev, newGig]);
    };
    const onEdit = async(id,updatedGig)=>{
        try{
            const formData = new FormData();
            for(const key in updatedGig){
                formData.append(key,updatedGig[key])
            }

            const res = await axios.put(
                `http://localhost:8080/api/gigs/${id}`,
                formData,
                {
                    headers:{
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "multipart/form-data",
                    },
                }
            );
            setGigs((prev)=>
            prev.map((gig)=>(gig.id===id?res.data:gig))
            );
        }
        catch(err){
            console.error("update failed",err);
        }
    };

    const onDelete = async(id)=>{
        try{
            axios.delete(`http://localhost:8080/api/gigs/${id}`,{
                headers:{Authorization: `Bearer ${token}`}
            });
            setGigs((prev)=>prev.filter((gig)=>gig.id!==id));
        }
        catch(err){
            console.error("Deleted failed", err);
        }
    }
    return(
        <gigContext.Provider value={{
            gigs,search,category,filteredGigs,setSearch,setCategory,onEdit,onDelete,setGigs,addGig}}>
            {children}
        </gigContext.Provider>
    )
}
export default CustomGigContext;

