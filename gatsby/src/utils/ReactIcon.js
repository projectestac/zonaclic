// Utilities for react-icons, combining some properties of FontAwesomeIcon
// See: https://react-icons.github.io/react-icons/

// Import the desired icons
import { FaBlog, FaStickyNote, FaInfoCircle, FaRssSquare, FaCubes, FaUsers } from 'react-icons/fa';
import { FcShipped } from 'react-icons/fc';
import { JClic, JClicAuthor } from './JClicIcons';

// Associate each icon with a label
const ICONS = {
  'blog': FaBlog,
  'sticky-note': FaStickyNote,
  'info-circle': FaInfoCircle,
  'rss-square': FaRssSquare,
  'cubes': FaCubes,
  'users': FaUsers,
  'truck': FcShipped,
  'jclic': JClic,
  'jclic-author': JClicAuthor,
  'default': FaBlog,
};

// Default FontAwesome sizes
const SIZES = {
  'xs': '.75em',
  'sm': '.875em',
  'lg': '1.33em',	//Also applies vertical-align: -25%
  '2x': '2em',
  '3x': '3em',
  '4x': '4em',
  '5x': '5em',
  '6x': '6em',
  '7x': '7em',
  '8x': '8em',
  '9x': '9em',
  '10x': '10em',
};

// Props recognized by IconBase are: `size`, `color` and `title`
// See: https://github.com/react-icons/react-icons/blob/master/packages/react-icons/src/iconBase.tsx
//
// Usage <ReactIcon icon={label} size={xs,sm,lg,2x,3x... or a CSS value} color={CSS color} ... other properties />
export function ReactIcon({ icon, size, style = {}, ...props }) {

  const icoFunc = ICONS[icon] || ICONS['default'];
  const computedSize = size ? SIZES[size] || size : SIZES.sm;
  const computedStyle = {
    'verticalAlign': size === 'lg' ? '-0.225em' : '-0.125em',
    ...style,
  }

  return icoFunc({ size: computedSize, style: computedStyle, ...props });
}