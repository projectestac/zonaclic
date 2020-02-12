// Utilities for "Font Awesome" SVG icons
// See: https://fontawesome.com/
// See: https://github.com/FortAwesome/react-fontawesome#build-a-library-to-reference-icons-throughout-your-app-more-conveniently

import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

// Brand icons. Use 'fab|name' (ex: 'fab|apple') as a descriptor
import { faApple } from '@fortawesome/free-brands-svg-icons';

// Regular icons:
import { faBlog, faStickyNote, faInfoCircle, faRssSquare, faCubes } from '@fortawesome/free-solid-svg-icons';

// Build the icon library
import { library } from '@fortawesome/fontawesome-svg-core';
library.add(
  faApple,
  faBlog, faStickyNote, faInfoCircle, faRssSquare, faCubes
);

/**
 * Gets a React element containing a Font Awesome SVG icon based on a declared name (or a default icon when no name is provided)
 * All used icons should be explicity added to `library` (see above)
 */
export const FontAwIcon = (props) => {
  const { icon } = props;
  const iconTag = icon ? icon.indexOf('|') > 0 ? icon.split('|') : icon : 'sticky-note';
  return <FontAwesomeIcon {...{ ...props, icon: iconTag }} />
}
