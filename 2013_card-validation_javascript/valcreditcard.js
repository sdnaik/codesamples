function isVisa(cc)
{
  if (((cc.length == 16) || (cc.length == 13)) &&
      (cc.substring(0,1) == 4))
  return true;
}  

function isMasterCard(cc)
{
  firstdig = cc.substring(0,1);
  seconddig = cc.substring(1,2);
  if ((cc.length == 16) && (firstdig == 5) &&
      ((seconddig >= 1) && (seconddig <= 5)))
  return true;

} 

function isAmericanExpress(cc)
{
  firstdig = cc.substring(0,1);
  seconddig = cc.substring(1,2);
  if ((cc.length == 15) && (firstdig == 3) &&
      ((seconddig == 4) || (seconddig == 7)))
  return true;

} 


function isDiscover(cc)
{
  first4digs = cc.substring(0,4);
  if ((cc.length == 16) && (first4digs == "6011"))
  return true;

} 

function isAnyCard(cc)
{
  if (!isMasterCard(cc) && !isVisa(cc) && !isAmericanExpress(cc) &&
      !isDiscover(cc)) {
    return false;
  }
  return true;

}        
