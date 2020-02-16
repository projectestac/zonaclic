/**
 * Combines a potential `className` field passed in `props` with the element
 * class name specified in `classes.root`
 * @param {Object} props - Inherited properties. Can contain a `className` property. Can also be _null_.
 * @param {Object} classes - Class set to be used. Only the `root` element, if exists, will be re-factorized.
 * @param {String=} root - Optional parameter with an alternative name for the `root` key.
 */
export const mergeClasses = (props, classes, root = 'root') => {
  if (props && props.className && classes && classes[root])
    classes[root] = `${classes[root]} ${props.className}`;
  return classes;
}

/**
 * Checks if the given expression is an absolute URL
 * (implemented with a very simple test, not RFC 3987 compliant!)
 * @param {string} text 
 */
export const isAbsoluteUrl = (text) => /^https?:\/\//.test(text);

export const htmlContent = (desc) => /<\w*>/.test(desc) ? desc : desc.replace(/\n/g, '<br/>\n');

export const getImgUrl = ({ siteMetadata: { baseUrl, pathPrefix, cardFileName }, slug, lang, thumbnail }) => {
  return thumbnail && thumbnail?.childImageSharp?.fluid?.src
    ? `${baseUrl}${thumbnail.childImageSharp.fluid.src}`
    : slug
      ? `${baseUrl}${pathPrefix}/${lang}${slug}${cardFileName}`
      : null;
}
