
function botoOn(id) {botoOnDoble(id, id);}

function botoOff(id) {botoOffDoble(id, id);}

function botoOnDoble(idObjecte,idImatge)
{
	if  (document.images)
	  eval ("document." + idObjecte + ".src=\"" + ico_base + idImatge + "_on.gif" + "\";");  
}

function botoOffDoble(idObjecte,idImatge)
{
    if  (document.images)
	  eval ("document." + idObjecte + ".src=\"" + ico_base + idImatge + ".gif" + "\";");
}
  
