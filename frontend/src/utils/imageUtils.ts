export const getImageUrl = (imagePath: string | null | undefined): string => {
    if (!imagePath) return 'https://via.placeholder.com/400x300';

    // If multiple images (comma separated), take the first one
    const mainImage = imagePath.split(',')[0];

    // If it's already a web URL or data URI, return as is
    if (mainImage.startsWith('http') || mainImage.startsWith('data:')) {
        return mainImage;
    }

    // Fix legacy Windows file paths
    // Convert backslashes to forward slashes
    let cleanPath = mainImage.replace(/\\/g, '/');

    // Return the cleaned path. We expect backend to handle full URLs or 
    // frontend components might need to prepend base URL if it's relative?
    // Current PropertyService returns "http://localhost:8081/uploads/...", 
    // so it should fall into the startsWith('http') block above.
    // If it falls through here, it might be a raw file path that we can't display anyway.

    return mainImage;
};
