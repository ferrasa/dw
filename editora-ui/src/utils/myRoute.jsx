import { useParams } from 'react-router-dom';


// Para obter parâmetros passados via Router v6
// Ex.: (em) this.props.match.params.id
export default function withRouter(Componente){
    return(props)=>{
       const p  = {params: useParams()};
       return <Componente {...props}  match = {p}/>
   }
 }

   
