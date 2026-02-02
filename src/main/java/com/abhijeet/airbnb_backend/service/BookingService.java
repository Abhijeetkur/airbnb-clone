package com.abhijeet.airbnb_backend.service;

import com.abhijeet.airbnb_backend.dto.BookingRequest;
import com.abhijeet.airbnb_backend.dto.BookingResponse;
import com.abhijeet.airbnb_backend.dto.GuestSelectionDTO;
import com.abhijeet.airbnb_backend.entity.booking.Booking;
import com.abhijeet.airbnb_backend.entity.booking.BookingGuest;
import com.abhijeet.airbnb_backend.entity.booking.BookingGuestFkId;
import com.abhijeet.airbnb_backend.entity.booking.BookingStatus;
import com.abhijeet.airbnb_backend.entity.booking.GuestType;
import com.abhijeet.airbnb_backend.entity.property.Property;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.exception.PropertyNotFoundException;
import com.abhijeet.airbnb_backend.repository.booking.BookingRepository;
import com.abhijeet.airbnb_backend.repository.booking.BookingStatusRepository;
import com.abhijeet.airbnb_backend.repository.booking.GuestTypeRepository;
import com.abhijeet.airbnb_backend.repository.property.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private BookingStatusRepository statusRepository;
    @Autowired
    private GuestTypeRepository guestTypeRepository;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, User guest) {
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new PropertyNotFoundException(
                        "Property with ID " + request.getPropertyId() + " not found"));

        // 1. Availability Check
        if (bookingRepository.isBooked(property.getId(), request.getStartDate(), request.getEndDate())) {
            throw new RuntimeException("Dates are already taken!");
        }

        // 2. Price Calculation
        long nights = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        BigDecimal totalAmount = property.getPrice().multiply(BigDecimal.valueOf(nights));

        // 3. Save Booking
        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setGuest(guest);
        booking.setCheckInDate(request.getStartDate());
        booking.setCheckOutDate(request.getEndDate());
        booking.setTotalAmount(totalAmount);
        BookingStatus confirmedStatus = statusRepository.findByStatusName("CONFIRMED")
                .orElseThrow(() -> new RuntimeException("Status 'CONFIRMED' not found in database"));
        booking.setBookingStatus(confirmedStatus);

        // 4. Handle Guests
        if (request.getGuestSelections() != null && !request.getGuestSelections().isEmpty()) {
            java.util.List<BookingGuest> bookingGuests = new java.util.ArrayList<>();
            for (GuestSelectionDTO selection : request.getGuestSelections()) {
                GuestType gType = guestTypeRepository
                        .findByTypeName(selection.getGuestType())
                        .orElseThrow(
                                () -> new RuntimeException("Guest type " + selection.getGuestType() + " not found"));

                BookingGuest bGuest = new BookingGuest();
                // Note: booking ID will be set after save or handled by @MapsId
                bGuest.setBooking(booking);
                bGuest.setGuestType(gType);
                bGuest.setGuestCount(selection.getCount());
                bGuest.setBookingGuestFkId(
                        new BookingGuestFkId(null, gType.getId()));
                bookingGuests.add(bGuest);
            }
            booking.setBookingGuests(bookingGuests);
        }

        return mapToResponse(bookingRepository.save(booking));
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .propertyId(booking.getProperty().getId())
                .propertyName(booking.getProperty().getPropertyName())
                .locationDisplay(booking.getProperty().getLocation().getLocationName())
                .hostName(booking.getProperty().getHost().getFname() + " " + booking.getProperty().getHost().getLname())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getBookingStatus().getStatusName())
                .guestSelections(booking.getBookingGuests() != null ? booking.getBookingGuests().stream()
                        .map(bg -> {
                            GuestSelectionDTO dto = new GuestSelectionDTO();
                            dto.setGuestType(bg.getGuestType().getTypeName());
                            dto.setCount(bg.getGuestCount());
                            return dto;
                        }).toList() : null)
                .build();
    }
}
